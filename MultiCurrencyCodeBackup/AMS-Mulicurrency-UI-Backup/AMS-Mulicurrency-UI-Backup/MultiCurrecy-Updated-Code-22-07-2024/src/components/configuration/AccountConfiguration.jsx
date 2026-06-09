import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import { v4 as uuidv4 } from "uuid";
import amsApi from "../../api/amsApi";
import { Link } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function ViewGLaccount() {
const [alldata, setAlldata] = useState([]);
 const [searchQuery, setSearchQuery] = useState("");
 const [itemPage, setItemPage] = useState(20);
 const [tcount, setTcount] = useState(1);
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    handleSubmit();
  }, [tcount, itemPage]);
  const handleSubmit = async () => {
    try {
      const response = await amsApi.get(
        `accountType/getAccountTyp/pagination/${itemPage}/${tcount}`
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.accountTypeMaster2);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };


     useEffect(() => {
       if (searchQuery === "" || searchQuery === " ") {
         handleSubmit();
       }
     }, [searchQuery]);

     const Search = async () => {
       try {
         const response = await amsApi.post(
           `accountType/accountTypeSearch/pagination/20/1`,
           {
             keyword: searchQuery,
           }
         );
         if (response.data.code === "S0000") {
           setAlldata(response.data.acctTypeSearch);
           // handleShowSuccess(response.data.message);
         } else {
           handleShowError(response.data.message);
         }
       } catch (error) {
         // handleShowError(error.response.data.message);
       }
     };

  // sorted table data
  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...alldata].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
    }
  });

  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-800">
          <div className="px-4 sm:px-10 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                View Account Type Configuration
              </p>
            </div>
          </div>
        </div>
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}

        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />
        <div className="overflow-x-auto relative shadow-md ">
          {/* {filteredData.length > 0 ? ( */}
          <>
            <div className="table-wrp block max-h-[33rem] ">
              <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                  <th
                    scope="col"
                    className="py-2 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strAccountType")}
                  >
                    ACCOUNT Type {renderSortArrow("strAccountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-2 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strDescription")}
                  >
                    DESCRIPTION {renderSortArrow("strDescription")}
                  </th>
                  <th
                    scope="col"
                    className="py-2 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strAccNumLength")}
                  >
                    ACCOUNT NO LENGTH {renderSortArrow("strAccNumLength")}
                  </th>
                  <th
                    scope="col"
                    className="py-2 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strAccNumStartDigit")}
                  >
                    ACCOUNT START DIGIT {renderSortArrow("strAccNumStartDigit")}
                  </th>
                  <th
                    scope="col"
                    className="py-2 px-6 whitespace-nowrap"
                    onClick={() => handleSort("strStatus")}
                  >
                    STATUS {renderSortArrow("strStatus")}
                  </th>
                  <th scope="col" className="py-2 px-6 whitespace-nowrap">
                    EDIT
                  </th>
                </thead>

                <tbody>
                  <>
                    {sortedData.map(
                      ({
                        strAccountType,
                        strDescription,
                        strAccNumLength,
                        strAccNumStartDigit,
                        strStatus,
                      }) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                        >
                          <td className="px-6 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {strAccountType || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {strDescription || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900 text-center">
                              {strAccNumLength || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900 text-center">
                              {strAccNumStartDigit || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {strStatus || "-"}
                            </div>
                          </td>
                          <td className="px-6 py-2 whitespace-nowrap">
                            <Link
                              title="Clcik Here To Link"
                              to={`/editaccount/${strAccountType}`}
                            >
                              <svg
                                xmlns="http://www.w3.org/2000/svg"
                                fill="none"
                                viewBox="0 0 24 24"
                                strokeWidth={1.5}
                                stroke="blue"
                                className="w-6 h-6"
                              >
                                <path
                                  strokeLinecap="round"
                                  strokeLinejoin="round"
                                  d="M16.862 4.487l1.687-1.688a1.875 1.875 0 112.652 2.652L10.582 16.07a4.5 4.5 0 01-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 011.13-1.897l8.932-8.931zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0115.75 21H5.25A2.25 2.25 0 013 18.75V8.25A2.25 2.25 0 015.25 6H10"
                                />
                              </svg>
                            </Link>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
