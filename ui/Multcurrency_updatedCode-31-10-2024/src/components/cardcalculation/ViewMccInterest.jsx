import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "../reports/AutoPagintationReport";
export default function ViewMccInterest() {
  const [list, setList] = useState([]);
 
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
    Selected();
  }, [tcount, itemPage]);

  const Selected = async () => {
    try {
      const response = await amsApi.post(
        `mcc-wise-interest/getMcWiseInterestView/pagination/${itemPage}/${tcount}`,
        {}
      );
      if (response.data.code === "S0000") {
        setList(response.data);
       
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };


    useEffect(() => {
      if (searchQuery === "" || searchQuery === " ") {
        Selected();
      }
    }, [searchQuery]);

    const Search = async () => {
      try {
        const response = await amsApi.post(
          `gl-account-type/getGlAccountTypeSearch/pagination/${itemPage}/${tcount}`,
          {
            keyword: searchQuery,
          }
        );
        if (response.data.code === "S0000") {
          setList(response.data);
          // handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    };


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
  const sortedData = [...list].sort((a, b) => {
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
        <div className="w-full shadow-md mt-2  ">
          <div className="px-4 sm:px-10  bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                MCC Wise Interest
              </p>
            </div>
          </div>
        </div>

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={list[0]?.strTotalCount}
          reCallApi={Selected}
        /> */}
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={list[0]?.strTotalCount}
          reCallApi={Search}
        />
        {/* 
        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={Selected}
        /> */}

        <div className="bg-white overflow-x-auto relative shadow-md  ">
          <div className="table-wrp block max-h-[27rem]">
            <table className="w-full text-sm text-left text-clack dark:text-blue-100">
              <thead className="text-xs border-b sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th
                    scope="col"
                    className="py-3 px-6"
                    onClick={() => handleSort("strAccountType")}
                  >
                    ACCOUNT Type {renderSortArrow("strAccountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-6"
                    onClick={() => handleSort("strMccCode")}
                  >
                    MCC CODE {renderSortArrow("strMccCode")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-6"
                    onClick={() => handleSort("strInterestRate")}
                  >
                    INTERST RATE {renderSortArrow("strInterestRate")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-6"
                    onClick={() => handleSort("strGracePeriod")}
                  >
                    GRACE PERIOD {renderSortArrow("strGracePeriod")}
                  </th>
                  <th
                    scope="col"
                    className="py-3 px-6"
                    onClick={() => handleSort("strPaymentReceivedWithinDays")}
                  >
                    PAYMENT RECEIVED WITHIN{" "}
                    {renderSortArrow("strPaymentReceivedWithinDays")}
                  </th>
                </tr>
              </thead>
              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map((value) => (
                      <tr
                        key={uuidv4()}
                        className="border-b dark:border-neutral-500"
                      >
                        <td className="whitespace-nowrap px-6 py-3">
                          <span className="text-sm font-medium text-gray-900">
                            {value.strAccountType || "-"}
                          </span>
                        </td>
                        <td className="px-6 py-3 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.strMccCode || "-"}
                          </span>
                        </td>
                        <td className="px-6 py-3 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.strInterestRate || "-"}
                          </span>
                        </td>
                        <td className="px-6 py-3 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.strGracePeriod || "-"}
                          </span>
                        </td>
                        <td className="px-6 py-3 whitespace-nowrap">
                          <span className="text-sm font-medium text-gray-900">
                            {value.strPaymentReceivedWithinDays || "-"}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-6 py-3 text-center">
                      <span className="text-sm font-medium text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
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
