import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect } from "react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
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
      const response = await amsApi.post(
        `gl-account-type/getGlAccountTypeData/pagination/${itemPage}/${tcount}`,
        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.glAccountviewModels);
        // handleShowSuccess(response.data.message);
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
        `gl-account-type/getGlAccountTypeSearch/pagination/${itemPage}/${tcount}`,
       {
   keyword:searchQuery  
}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.viewGLAcctSearch);
        // handleShowSuccess(response.data.message);
       
        } else {
          handleShowError(response.data.message);
         
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    
  };

  // sorted tabled data
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
      <div className="max-w-7xl mx-auto h-full">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-1 sm:px-1 bg-blue-800">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                View GL Account
              </p>
            </div>
          </div>
        </div>
        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={filteredData[0]?.strTotalCount}
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
         
            <div className="table-wrp block max-h-[32rem] ">
              <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                  <th
                    scope="col"
                    className="py-1 px-5 whitespace-nowrap"
                    onClick={() => handleSort("strGLAccountType")}
                  >
                    GL Account Type {renderSortArrow("strGLAccountType")}
                  </th>
                  <th
                    scope="col"
                    className=" px-5 py-1 whitespace-nowrap"
                    onClick={() => handleSort("strGLAccountDescription")}
                  >
                    GL Account Description{" "}
                    {renderSortArrow("strGLAccountDescription")}
                  </th>
                  <th
                    scope="col"
                    className="py-1   px-5 whitespace-nowrap"
                    onClick={() => handleSort("strAccountNumber")}
                  >
                    Account Number {renderSortArrow("strAccountNumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-5   whitespace-nowrap"
                    onClick={() => handleSort("strOpeningBalance")}
                  >
                    Opening Balance {renderSortArrow("strOpeningBalance")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-5 whitespace-nowrap"
                    onClick={() => handleSort("strClosingBalance")}
                  >
                    Closing Balance {renderSortArrow("strClosingBalance")}
                  </th>
                </thead>
                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map(
                        ({
                          strGLAccountType,
                          strGLAccountDescription,
                          strAccountNumber,
                          strOpeningBalance,
                          strClosingBalance,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="border-b dark:border-neutral-500"
                          >
                            <td className="  px-5 py-1 whitespace-nowrap">
                              {strGLAccountType || "-"}
                            </td>
                            <td className="  px-5 py-1 whitespace-nowrap">
                              {strGLAccountDescription || "-"}
                            </td>
                            <td className="  px-5 py-1 whitespace-nowrap">
                              {strAccountNumber || "-"}
                            </td>
                            <td className="  px-5 py-1 text-center whitespace-nowrap">
                              {strOpeningBalance || "-"}
                            </td>
                            <td className="  px-5 py-1 text-center whitespace-nowrap ">
                              {strClosingBalance || "-"}
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="12" className=" px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500 ">
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
