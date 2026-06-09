import React, { useEffect, useState } from "react";
import AppLayout from "../../layout/AppLayout";
import { v4 as uuidv4 } from "uuid";
import { NavLink } from "react-router-dom";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "../reports/AutoPagintationReport";

export default function AuthorizeJournalTransfer() {
  const [alldata, setAlldata] = useState([]);
  const [searchKeyword, setSearchKeyword] = useState("");
  const [checkpoint1, setCheckpoint1] = useState(0);
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
 const [searchQuery, setSearchQuery] = useState("");
 const [itemPage, setItemPage] = useState(20);
 const [tcount, setTcount] = useState(1);

 

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
  }, []);

  const handleSubmit = async () => {
    try {
      const response = await amsApi.post(
        `journalTransfer/getAccountauthoriselst`,
        {}
      );
      if ((response.data.code = "S0000")) {
        setAlldata(response.data.journalTransferList);
      
        // setTcount(response.data[0].tcount);
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
        handleSubmit();
      }
    }, [searchQuery]);

    const Search = async () => {
      try {
        const response = await amsApi.post(
          `journalTransfer/authJournalTransferSearch/pagination/${itemPage}/${tcount}`,
          {
            keyword: searchQuery,
          }
        );
        if (response.data.code === "S0000") {
          setAlldata(response.data.authJournalTransferSearch);
          // handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    };

  // sorted table data
  const [sortOrder, setSortOrder] = useState("asc");
  const [sortColumn, setSortColumn] = useState("");
  const handleSort = (column) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
  };

  const sortedData = alldata.slice(0).sort((a, b) => {
    const dateA = new Date(a.strTxnDate);
    const dateB = new Date(b.strTxnDate);
    const timeA = new Date("1970/01/01 " + a.strTxnTime);
    const timeB = new Date("1970/01/01 " + b.strTxnTime);

    if (dateA.getTime() === dateB.getTime()) {
      return sortOrder === "asc" ? timeA - timeB : timeB - timeA;
    } else {
      return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
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
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-800 ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                Authorize Journal Transfer
              </p>
            </div>
          </div>
        </div>
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}
        <div className="bg-white overflow-x-auto relative  ">
          <div className="table-wrp block lg:max-h-[33rem] md:max-h-[33rem]">
            <table className="w-full text-xs  text-left text-black dark:text-blue-100">
              <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                <tr>
                  <th
                    scope="col"
                    className="py-1 px-6  whitespace-nowrap"
                    onClick={() => handleSort("strTxnId")}
                  >
                    txn ID {renderSortArrow("strTxnId")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-6  whitespace-nowrap"
                    onClick={() => handleSort("strTxnDate")}
                  >
                    txn DATE {renderSortArrow("strTxnDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-6  whitespace-nowrap"
                    onClick={() => handleSort("strTxnTime")}
                  >
                    txn TIME {renderSortArrow("strTxnTime")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-6  whitespace-nowrap"
                    onClick={() => handleSort("strAmoutToTransfer")}
                  >
                    AMOUNT {renderSortArrow("strAmoutToTransfer")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-6  whitespace-nowrap"
                    onClick={() => handleSort("strMakerId")}
                  >
                    MAKER USER ID {renderSortArrow("strMakerId")}
                  </th>
                </tr>
              </thead>
              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map(
                      ({
                        strTxnId,
                        strTxnDate,
                        strTxnTime,
                        strAmoutToTransfer,
                        strMakerId,
                      }) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                        >
                          <td className="px-6 py-1 whitespace-nowrap">
                            <span className="text-xs font-medium text-blue-600">
                              <NavLink
                                to={`/approve-journal-transfer/${strTxnId}`}
                                className=" shadow-md p-2 bg-white text-black  rounded-full focus:ring-blue-400"
                              >
                                {strTxnId || "-"}
                              </NavLink>
                            </span>
                          </td>
                          <td className="px-6 py-1 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {strTxnDate || "-"}
                            </span>
                          </td>
                          <td className="px-6 py-1 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {strTxnTime || "-"}
                            </span>
                          </td>
                          <td className="px-6 py-1 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {strAmoutToTransfer || "-"}
                            </span>
                          </td>
                          <td className="px-6 py-1 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {strMakerId || "-"}
                            </span>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                ) : (
                  <tr>
                    <td colSpan="6" className="px-6 py-1 text-center">
                      <span className="text-lg font-normal text-gray-500">
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
      {/* </div> */}
    </AppLayout>
  );
}
